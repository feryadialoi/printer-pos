package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.rules.annotations

/**
 * refers to property name want to compare
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class Equal(val value: String)
