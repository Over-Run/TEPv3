# The Experimental Project v3 [![Java CI with Gradle](https://github.com/Over-Run/TEPv3/actions/workflows/gradle.yml/badge.svg?branch=3.0&event=push)](https://github.com/Over-Run/TEPv3/actions/workflows/gradle.yml)

[English](README.md) · [简体中文](README-zh-cn.md)

## 这是什么？

这是 The Experimental Project v3，一个使用了 OpenGL 3.2 核心选项的游戏（引擎）。

## 为什么

我们注意到，其他游戏引擎非常庞大，动不动就以 GB 为单位。所以就出现了这个项目。

## 如何使用

加入到依赖项：

```groovy
dependencies {
    // 将 ${current_version} 替换为 TEPv3 的当前版本
    implementation 'io.github.over-run:tepv3:${current_version}'
}
```

## JVM 参数

~~没有~~

## 特别注意

```properties
Material=材质
Texture=纹理
Map=贴图（多数时候）
```

如果没分清楚可能会导致光照系统出错。
