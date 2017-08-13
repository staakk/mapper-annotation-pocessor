# mapper-annotation-pocessor
Annotation processor for mapping one model class to another.
I wrote it for practising annotation processing.

It maps fields values from one class to another if they are public and have the same names or have public getters/setters methods.

## How it works

There are two annotation which you can use:

* `@Map` you can annotate any static class with it, you should specify value argument for it of type `Class` (this class also has to be static). 
Annotation processor will generate `FooToBarMapper` class where `Foo` is the name of annotated class and `Baz` is name of class specified
as a value for this annotation.
Generated class will contain method `Bar map(Foo foo)` which produces `Bar` object.
All fields in `Bar` object will have the same values as fields in `foo` object with the same names. 

* `@Exclude` fields or getters annotated with `@Exclude` won't be mapped.
