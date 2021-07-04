package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.rules.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class Label(val value: String = "")
