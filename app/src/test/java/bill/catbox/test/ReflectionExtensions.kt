package bill.catbox.test

import java.lang.reflect.Field
import java.lang.reflect.Modifier

fun Any.forceSet(fieldName: String, value: Any) = deferring { defer ->
    val field = javaClass.findField(fieldName)
    if (field.isAccessible == false) {
        field.isAccessible = true
        defer {
            field.isAccessible = false
        }
    }

    if (field.modifiers and Modifier.FINAL.inv() != 0) {
        val modifiers = Field::class.java.getDeclaredField("modifiers")
        modifiers.isAccessible = true
        modifiers.setInt(field, field.modifiers and Modifier.FINAL.inv())
        defer {
            modifiers.setInt(field, field.modifiers or Modifier.FINAL)
            modifiers.isAccessible = false
        }
    }


    field.set(this, value)
}

private fun Class<in Any>.findField(fieldName: String): Field =
        getDeclaredFieldOrNull(fieldName)
                ?: superclass?.findField(fieldName)
                ?: throw NoSuchFieldException(fieldName)

private fun Class<Any>.getDeclaredFieldOrNull(fieldName: String) =
        if (declaredFields.any { it.name == fieldName }) {
            getDeclaredField(fieldName)
        } else {
            null
        }
