package skillima.mentors.utils.validator
import android.util.Patterns

object EmailValidator {
    fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}