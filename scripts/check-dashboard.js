const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');

const root = path.resolve(__dirname, '..');
const indexPath = path.join(root, 'index.html');
const generatedStatePath = path.join(root, 'workflow', 'state.generated.js');
const html = fs.readFileSync(indexPath, 'utf8');
const generatedState = fs.readFileSync(generatedStatePath, 'utf8');
const errors = [];

try {
  new vm.Script(generatedState, { filename: 'workflow/state.generated.js' });
} catch (error) {
  errors.push(`state.generated.js 语法错误：${error.message}`);
}

if (!html.includes('src="workflow/state.generated.js"')) {
  errors.push('index.html 未加载 workflow/state.generated.js');
}

const inlineScripts = [...html.matchAll(/<script(?![^>]*\bsrc=)[^>]*>([\s\S]*?)<\/script>/gi)];
if (!inlineScripts.length) errors.push('index.html 缺少内联渲染脚本');
for (const [index, match] of inlineScripts.entries()) {
  try {
    new vm.Script(match[1], { filename: `index.html:inline-script-${index + 1}` });
  } catch (error) {
    errors.push(`index.html 内联脚本语法错误：${error.message}`);
  }
}

if (/这里粘贴 P\d+|\{粘贴同一套/.test(html)) {
  errors.push('index.html 仍包含手工 Lovart 占位提示词');
}

if (errors.length) {
  console.error('看板静态检查失败：');
  errors.forEach((error) => console.error(`- ${error}`));
  process.exit(1);
}

console.log('看板状态文件和脚本语法检查通过');
