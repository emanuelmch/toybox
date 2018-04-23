package bill.catbox.infra

import android.content.Context
import android.preference.EditTextPreference
import android.util.AttributeSet

//FIXME: Should have a better UI
class EditIntPreference : EditTextPreference {

    @JvmOverloads constructor(context: Context?,
                              attrs: AttributeSet? = null,
                              defStyleAttr: Int = android.R.attr.editTextPreferenceStyle
    ) : super(context, attrs, defStyleAttr)

    override fun getPersistedString(defaultReturnValue: String?) = getPersistedInt(-1).toString()

    override fun persistString(value: String) = persistInt(value.toInt())
}
