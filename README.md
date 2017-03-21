# Material Design Toolbox

[![Release](https://jitpack.io/v/nasahapps/Material-Design-Toolbox.svg)](https://jitpack.io/#nasahapps/Material-Design-Toolbox)

*_This library is still a work in progress._*

The main purpose of this library is to provide components and helper methods in implementing [Material Design concepts](http://www.google.com/design/spec/material-design/introduction.html) with little to no effort. This library in particular focuses on components that are not yet included in Google's support libraries, with most widgets adhering very closely to the guidelines Google has set. Links to already-existing components will be provided.

This library supports Android 4.1 (API 16) and up, however, not all components/methods will be supported pre-API 21.

To use, add Jitpack as a repository in your top-level build.gradle file:

```groovy
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}
```
Then add one (or all) of the following dependencies, depending on what you want specifically:

```groovy
dependencies {
    // For general utility methods/classes
    compile 'com.github.nasahapps.Material-Design-Toolbox:base:{latest_version}' 
    // For helper methods pertaining to BottomSheets
    compile 'com.github.nasahapps.Material-Design-Toolbox:bottomsheets:{latest_version}' 
}
```

Note that all modules include the `com.github.nasahapps.Material-Design-Toolbox:base` module by default.

* [Components](#components)
  * [Bottom Navigation](#bottom-navigation)
  * [Bottom Sheets](#bottom-sheets)
  * [Buttons](#buttons)
  * [Steppers](#steppers)
  * [Tabs](#tabs)
  * [Text Fields](#text-fields)
  * [Toolbars](#toolbars)
  * [Tooltips](#tooltips)

## Components

### [Bottom Sheets](https://material.io/guidelines/components/bottom-sheets.html)

Dependency: `compile 'com.github.nasahapps.Material-Design-Toolbox:bottomsheets:{latest_version}'`

Bottom sheets are provided by the `design` module in the support library, by the use of [BottomSheetDialog](https://developer.android.com/reference/android/support/design/widget/BottomSheetDialog.html),
[BottomSheetDialogFragment](https://developer.android.com/reference/android/support/design/widget/BottomSheetDialogFragment.html),
or with your own custom View in a CoordinatorLayout using a [BottomSheetBehavior](https://developer.android.com/reference/android/support/design/widget/BottomSheetBehavior.html).

The Material Design Toolbox, however, provides a utils class that will create [a single-choice list styled as a BottomSheetDialog](https://developer.android.com/guide/topics/ui/dialogs.html#AddingAList)
in a similar fashion to how you can already create a single-choice Dialog.

For example, the following code will create and show a BottomSheetDialog with a title and its items aligned in a grid.

```java
new BottomSheetDialogBuilder(context, useGrid)
        .setTitle(title)
        .setItems(items, onClickListener)
        .show();
```

When initializing the `BottomSheetDialogBuilder`, you pass in a `Context` and whether you want the items to be laid
out in a list or in a grid.

The type of item the BottomSheetDialogBuilder accepts is a `BottomSheetItem`, which takes a `CharSequence` title and a `Drawable` icon.
You can also pass in a menu resource instead:

`builder.setItems(R.menu.bottom_sheet, listener)`

The item click listener is very similar to the standard Dialog's OnClickListener:

`void onClick(DialogInterface dialog, int position, int itemId);`

In the `onClick` callback, if you passed in a menu resource instead of a list of items, you can use `itemId`
to refer to the selected item's menu id you set in XML. Else, use `position` for the item's list position.

If you want the icons to all be tinted a certain color, you can use `builder.setIconTint(colorInt)`. Also
you can use `builder.create()` to build the Dialog but not show it yet in case you want to alter the
Dialog any further.

### [Buttons](https://material.io/guidelines/components/buttons.html)

[Buttons](https://developer.android.com/reference/android/widget/Button.html) are a native view widget in the Android SDK.

By default, a Button is untinted.

![](images/button_untinted.png)

To have a tinted Button (default color: `colorAccent`), in XML you can set the Button's style:

`style="@style/Widget.AppCompat.Button.Colored"`

and this will automatically tint your button's color to your theme's `colorAccent`.

![](images/button_tinted_default.png)

If you want a color other than `colorAccent` you also must include a custom theme to the Button. For example:

```xml
<style name="CustomButton" parent="@style/ThemeOverlay.AppCompat">
    <item name="colorAccent">#f00</item>
</style>

<Button
    style="@style/Widget.AppCompat.Button.Colored"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Button"
    android:theme="@style/CustomButton"/>
```

This results in the following:

![](images/button_tinted_custom.png)

To have a disabled button, in XML set `android:enabled="false"`, or in Java, `button.setEnabled(false)`

![](images/button_disabled.png)

For a flat-styled button, use `style="@style/Widget.AppCompat.Button.Borderless"` for plain black/white text,
 or `style="@style/Widget.AppCompat.Button.Borderless.Colored"` for the text color to be `colorAccent`

![](images/button_flat.png)

For a rounded button, use the [FloatingActionButton](https://developer.android.com/reference/android/support/design/widget/FloatingActionButton.html)
which is part of the `design` module of the support library.

![](images/button_fab.png)

It automatically themes to your `colorAccent` value. To change this, in XML set `app:backgroundTint="color"`
or in java, `fab.setBackgroundTintList(colorStateList)`.

If you want a mini FAB, in XML set `app:fabSize="mini"` or in java, `fab.setSize(SIZE_MINI)`

![](images/button_fab_mini.png)

The Material Design guidelines also define a "dropdown" button, which is natively just a [Spinner](https://developer.android.com/reference/android/widget/Spinner.html)
in the Android SDK.

### [Steppers](http://www.google.com/design/spec/components/steppers.html)

This library provides two stepper layouts:

`StepperProgressLayout`: Displays step progress using plain text (e.g. Step 4 of 6), dots, or with a tinted ProgressBar. The `StepperProgressLayout` provides "Back" and "Next" buttons for going forwards/backwards between steps, with the "Next" button turning into a "Finish" button on the last step. Also, it is a `ViewGroup`, so any views you add to it will automatically be placed accordingly. (TODO: add pictures)  

Custom attributes: 
* stepperMaxProgress 
 * the max number of steps
 * default value: 1
* stepperProgress
 * the current step 
 * default value: 1
* stepperBackButtonText 
 * default value: "Back"
* stepperNextButtonText
 * default value: "Next"
* stepperFinishButtonText
 * the text to be displayed on the "Next" button when on the last step
 * default value: "Finish"
* stepperProgressType
 * the type of progress to be shown, one of TYPE_TEXT, TYPE_DOTS, or TYPE_BAR
 * default value: TYPE_TEXT
* stepperProgressAccent
 * the accent color to use for the dots/bar progress
 * default value: R.attr.colorPrimary

`HorizontalStepperLayout`: Displays steps using `Stepper` views in either a horizontal or vertical fashion. Steppers have one of four states: `COMPLETED`, `ACTIVE`, `INACTIVE`, or `ERROR`. 
Custom attributes:
* stepperLayoutMargin
 * the margin between each Stepper
 * default value: 56dp

Also provided is the `Stepper` widget that should be the only child view used in a `HorizontalStepperLayout`. 
Custom attributes:
* stepperTitle
* stepperSubtitle
* stepperAccent
 * the accent color to be used as the step number background
 * default value: R.attr.colorPrimary
* stepperState
 * the initial state of the stepper (`ACTIVE`, `INACTIVE`, `COMPLETED`, or `ERROR`)
 * default value: `ACTIVE`

### [Tabs](https://material.io/guidelines/components/tabs.html)

The [`TabLayout`](https://developer.android.com/reference/android/support/design/widget/TabLayout.html) widget is part of the `design` support library.

### [Text Fields](https://material.io/guidelines/components/text-fields.html)

The [`TextInputLayout`](https://developer.android.com/reference/android/support/design/widget/TextInputLayout.html) widget is part of the `design` support library. It is suggested to use a `TextInputEditText` instead of a regular `EditText` in this layout.

### [Toolbars](https://material.io/guidelines/components/toolbars.html)  

The [`Toolbar`](https://developer.android.com/reference/android/support/v7/widget/Toolbar.html) widget is part of the `appcompat-v7` support library.

### [Tooltips](https://material.io/guidelines/components/tooltips.html)  

Tooltips (extends `TextView`) are similar to [`Toasts`](https://developer.android.com/reference/android/widget/Toast.html), both in appearance and in function. Creating a `Tooltip` is very similar to creating a `Toast` or `Snackbar`:

```java
Tooltip.makeTooltip(context, "Tooltip text", Tooltip.LENGTH_SHORT, anchorView).show();
```  

The arguments passed in are a `Context`, string text (or a string resource), a duration of how long you want the `Tooltip` to appear (`LENGTH_SHORT` or `LENGTH_LONG`), and a `View` to anchor the `Tooltip` on. By default, the `Tooltip` will appear below the anchor, unless the anchor is low enough on the screen, then the `Tooltip` will instead appear above the anchor.
