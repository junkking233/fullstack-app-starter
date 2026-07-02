# 共享图标资源规范

本目录是项目的共享图标源库。开发 Web 前端或微信小程序时，必须先从这里选图标，再按需复制到对应子应用目录中使用。

## 目录说明

| 路径 | 用途 |
| --- | --- |
| `assets/icons/` | 共享 SVG 图标源库。 |
| `assets/icon-set.html` | 图标预览页，用于快速查看图标名称和样式。 |

## 强制使用规则

1. 开发页面前，先打开 `assets/icon-set.html` 或查看 `assets/icons/`，确认是否已有需要的图标。
2. 已有图标时，只复制实际用到的 SVG 到目标子应用目录，不要一次性复制整个图标库。
3. 没有合适图标时，先把新增 SVG 放入 `assets/icons/`，再复制到目标子应用目录使用。
4. 新增图标文件名使用小写 kebab-case，例如 `order-list.svg`。
5. SVG 应使用 `stroke="currentColor"` 或 `fill="currentColor"`，避免写死业务颜色。
6. 子应用内的图标文件是使用副本，共享源文件仍以 `assets/icons/` 为准。

## 复制目标

| 使用端 | 目标目录 | 示例 |
| --- | --- | --- |
| Web 前端 | `apps/frontend/src/assets/icons/` | `cp assets/icons/search.svg apps/frontend/src/assets/icons/search.svg` |
| 微信小程序 | `apps/miniprogram/assets/icons/` | `cp assets/icons/search.svg apps/miniprogram/assets/icons/search.svg` |

## 使用要求

- Web 前端中，可通过构建工具导入 SVG，或作为静态图片引用。
- 微信小程序中，复制后的图标应从 `apps/miniprogram/assets/icons/` 引用，例如 `/assets/icons/search.svg`。
- 如果一个业务图标会被多个端使用，必须先沉淀到 `assets/icons/`，再分别复制到各端。
- 不要在页面目录里临时散落图标文件，也不要只把新增图标放进某一个子应用而不更新共享源库。
