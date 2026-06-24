# 微信小程序

微信小程序端，包含首页、分类、购物车、订单、地址、优惠券、登录注册等页面。

> **可选模块**：如果项目不需要小程序，可以删除整个 `apps/wechat-miniprogram/` 目录，并从 `docker-compose.yml` 中移除相关配置（如有）。

## 常用命令

```bash
cd apps/wechat-miniprogram
npm install
```

然后用微信开发者工具导入 `apps/wechat-miniprogram`，并在工具里执行"构建 npm"。

## 目录结构

| 目录 | 说明 |
| --- | --- |
| `pages/` | 小程序页面。 |
| `services/` | 商品、购物车、订单等业务接口封装。 |
| `utils/` | 请求、登录态、格式化工具。 |
| `assets/` | 图片资源。 |

## 接口地址

本地接口地址在 `app.js` 的 `apiBaseUrl` 中配置。真机调试时，需要把它改成手机可访问的局域网或公网地址。

## 注意事项

- 使用 Vant Weapp UI 框架：https://vant-ui.github.io/vant-weapp/#/home
- 原生 `button` 仅用于需要微信原生能力的场景（`open-type`、授权、分享、获取手机号、表单提交等）。
- 普通页面操作按钮、底部固定操作栏按钮、卡片内按钮，优先使用 `view`/`text` 模拟按钮，并绑定 `bindtap`。
- 如必须使用原生 `button` 做自定义样式，必须完整重置：`margin: 0`、`padding: 0`、`border: 0`、`line-height`、`text-align`、`background`、`border-radius`、`button::after { border: 0; }`。
- 底部固定操作栏必须适配安全区：`padding-bottom: calc(基础间距 + env(safe-area-inset-bottom))`。
- 页面内容区必须为底部固定操作栏预留空间，避免最后一块内容被遮挡。
