# Goal 任务计划模板 GoalPlanTemplate

阶段状态不在本模板手工维护。`workflow/state.json` 是唯一状态源，运行 `python3 scripts/workflow.py sync` 后，GoalPlan 状态区和 `index.html` 会同步更新。

阶段只允许 `pending / in_progress / blocked / completed / not_applicable`。推进时只检查当前阶段 gates；未来阶段待开始项不阻塞当前阶段。阻塞必须写明恢复动作。

## 当前状态与门禁

正式 GoalPlan 中保留以下标记，由脚本生成内容：

```markdown
<!-- WORKFLOW:GENERATED:START -->
<!-- WORKFLOW:GENERATED:END -->
```

## ScopeBudget 记录

| 角色 | P0/P1 核心功能（最多 5 个） | P2/暂缓功能 | 合并/删减说明 | 状态 |
| --- | --- | --- | --- | --- |
|  |  |  |  | 待填写 |

| 端 | P0/P1 页面数 | 合并/删减说明 | 状态 |
| --- | --- | --- | --- |
| Web/小程序/APP |  |  | 待填写 |

## 外部能力、额度与设计确认

| 阶段 | 服务 | 实际能力标识 | 认证 | 额度/计费 | 模型/分辨率/画幅 | 用户批准批次 | 原模式/恢复结果 | 证据 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 2 | Lovart |  | 待检查 | 待检查 | `Seedream 5.0 Pro` / 1K / 待定 | 未批准 | 待记录 |  |
| 3 | Stitch |  | 待检查 | 待检查 |  | 未批准 | 不适用 |  |
| 3-4 | Figma |  | 待检查 |  |  | 不适用 | 不适用 |  |

Lovart 固定以 `Seedream 5.0 Pro` 生成 1K 单页稿，APP/小程序为 9:16、网页为 16:9；先做 1 张代表页并获得用户方向确认，再批量生成。切换模式前记录 `query-mode`，获批后使用 `set-mode --fast` 和页面线程 `--mode fast`，批次成功或失败退出后恢复原模式。Figma Frame 存在和用户设计确认是两个独立门禁。

## 页面与实现证据

### Git Worktree 并行任务记录

阶段 4 开始开发前先填写本表，明确采用串行开发还是 Git Worktree 并行开发。未启用 Worktree 时，至少保留一行说明“串行开发”和原因；启用时，每个并行任务必须有独立分支和项目目录外部的 worktree 路径。

Worktree 是独立工作区，未跟踪的 `.env`、`node_modules` 和构建产物不会共享。Worker 局部检查前必须准备本地环境，且不得提交这些本地文件。

| 任务 | 执行模式 | 分支 | Worktree 路径 | 修改范围 | 依赖 | 局部检查 | 合并状态 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 示例任务 | 串行 / Worktree | `ai/task-name` | `../项目-worktrees/task-name` | 页面/API/DB/测试范围 | 前置任务 | 待执行 | 待合并 / 不适用 |

| 页面 | Lovart Project/thread/输出 | Stitch screen/备份 | Figma Frame/nodeId | 用户设计确认 | 实现文件 | 对照结论 |
| --- | --- | --- | --- | --- | --- | --- |
| Pxx |  |  |  | 待确认 |  | 待验收 |

## API 与数据库

| 类型 | 名称 | 关联 P0/P1 功能 | 权限/异常/数据 | 实现位置 | 状态 |
| --- | --- | --- | --- | --- | --- |
| API |  |  |  |  | 待开始 |
| DB |  |  |  | `db/db.sql` | 待开始 |

## 缺陷与阻塞

| 编号 | 类型 | 内容 | 影响阶段 | 状态 | 恢复动作 |
| --- | --- | --- | --- | --- | --- |
| D1 | 缺陷/阻塞/待确认 |  |  | 待处理 |  |

## 阶段 5 检查

| 检查 | 命令/方式 | 结果 | 证据 |
| --- | --- | --- | --- |
| 工作流 | `python3 scripts/workflow.py validate` | 待执行 |  |
| 后端 | `mvn test` | 待执行 |  |
| 前端 | `npm run check` | 待执行 |  |
| 小程序 | `npm run check` | 待执行 |  |
| AI 服务 | `python3 -m unittest discover -s tests` | 待执行 |  |
| Worktree | `python3 scripts/workflow.py worktree-status`，并核对分支合并、冲突处理和临时 worktree 清理 | 待执行 |  |
| 页面/安全/Git | 浏览器、权限、数据、remote 与提交范围 | 待执行 |  |
