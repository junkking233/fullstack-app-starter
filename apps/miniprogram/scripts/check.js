const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');

const ROOT = path.resolve(__dirname, '..');
const EXPECTED_APP_ID = 'wxd84d204ed36b05b5';
const ignoredDirectories = new Set(['node_modules', 'miniprogram_npm']);
const errors = [];

function walk(directory) {
  const files = [];
  for (const entry of fs.readdirSync(directory, { withFileTypes: true })) {
    if (entry.isDirectory() && ignoredDirectories.has(entry.name)) continue;
    const target = path.join(directory, entry.name);
    if (entry.isDirectory()) files.push(...walk(target));
    else files.push(target);
  }
  return files;
}

function relative(file) {
  return path.relative(ROOT, file);
}

function checkJson(file) {
  try {
    JSON.parse(fs.readFileSync(file, 'utf8'));
  } catch (error) {
    errors.push(`${relative(file)} JSON 无效：${error.message}`);
  }
}

function checkJavaScript(file) {
  try {
    new vm.Script(fs.readFileSync(file, 'utf8'), { filename: relative(file) });
  } catch (error) {
    errors.push(`${relative(file)} JavaScript 语法错误：${error.message}`);
  }
}

function checkNativeButtons(file) {
  const source = fs.readFileSync(file, 'utf8');
  const buttons = source.match(/<button\b[^>]*>/gi) || [];
  for (const button of buttons) {
    if (!/\b(open-type|form-type)\s*=/.test(button)) {
      errors.push(`${relative(file)} 存在没有 open-type/form-type 的原生 button：${button}`);
    }
  }
}

const files = walk(ROOT);
files.filter((file) => file.endsWith('.json')).forEach(checkJson);
files.filter((file) => file.endsWith('.js')).forEach(checkJavaScript);
files.filter((file) => file.endsWith('.wxml')).forEach(checkNativeButtons);

const projectConfig = JSON.parse(fs.readFileSync(path.join(ROOT, 'project.config.json'), 'utf8'));
if (projectConfig.appid !== EXPECTED_APP_ID) {
  errors.push(`project.config.json appid 必须为 ${EXPECTED_APP_ID}`);
}

const appConfig = JSON.parse(fs.readFileSync(path.join(ROOT, 'app.json'), 'utf8'));
for (const page of appConfig.pages || []) {
  for (const extension of ['js', 'json', 'wxml', 'wxss']) {
    const pageFile = path.join(ROOT, `${page}.${extension}`);
    if (!fs.existsSync(pageFile)) errors.push(`页面文件缺失：${relative(pageFile)}`);
  }
}

if (errors.length) {
  console.error('小程序静态检查失败：');
  errors.forEach((error) => console.error(`- ${error}`));
  process.exit(1);
}

console.log(`小程序静态检查通过：${files.length} 个文件`);
