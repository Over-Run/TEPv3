# The Experimental Project v3

## What

Hi there. Here is the Experimental Project v3.

This is a game engine. It used OpenGL 3.2 core profile. Also, if the user computer doesn't support to OpenGL 3.2, the engine will use the GL 2.0 fallback.

## Why

We note that, the other game engine are very large. So we made this project.

## How

Just add to your project dependencies.

```groovy
dependencies {
    // Replace ${current_version} with current TEPv3 version
    implementation 'io.github.over-run:tepv3:${current_version}'
}
```

## The JVM Args

- `-Dtepv3.forceGL20=true`: Force using the GL 2.0 fallback
