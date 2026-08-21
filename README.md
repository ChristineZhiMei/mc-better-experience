# Better Experience

适用于 Minecraft 1.21.4 的 Fabric Mod，为原版设置菜单增加“更高的体验”入口。

## 功能

- 在 Minecraft 设置菜单中增加“更高的体验”菜单项；
- 使用滑动条设置经验获取倍率；
- 默认倍率为 `x1`，支持 `x0` 到 `x100` 的整数倍率；
- 配置保存在本地，并在进入世界或保存设置时同步到服务端；
- 只放大正数经验获取，不改变经验扣除行为。

多人游戏需要客户端和服务端同时安装本 Mod，经验倍率由服务端执行。

## 开发环境

- Minecraft 1.21.4
- Java 21
- Fabric Loader 0.16.12
- Fabric API 0.119.4+1.21.4

## 构建

```bash
./gradlew build
```

构建产物位于 `build/libs/`。
