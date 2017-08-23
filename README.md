# android\-xcolorpicker

Android的颜色选择器。

效果图：

![][1]

## Gradle

[![](https://www.jitpack.io/v/wuzhendev/android-xcolorpicker.svg)](https://www.jitpack.io/#wuzhendev/android-xcolorpicker)

```
repositories {
    maven {
        url "https://www.jitpack.io"
    }
}

dependencies {
    compile 'com.github.wuzhendev:android-xcolorpicker:x.y.z'
}
```

## Attrs：

``` xml
<!-- 当前颜色指示器的长度 -->
<attr name="xcp_pointerLength" format="dimension|reference" />

<!-- 当前颜色指示器的线条的宽度 -->
<attr name="xcp_pointerWidth" format="dimension|reference" />

<!-- 当前颜色指示器的颜色 -->
<attr name="xcp_pointerColor" format="color|reference" />
```

## Sample

[Sample sources][2]

[Sample APK](https://github.com/wuzhendev/android-xcolorpicker/raw/master/assets/XColorPicker_Demo_v1_0_0.apk)

## Future

1. 添加 `ColorPickerDialog`。
2. 支持多种样式。

## License

```
Copyright 2016 wuzhen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[1]: ./assets/1.jpg
[2]: ./samples
