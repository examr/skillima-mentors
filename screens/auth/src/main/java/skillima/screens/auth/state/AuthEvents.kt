package skillima.screens.auth.state

import skillima.core.module.UserData

sealed class AuthEvents {
    data class OnEmailChange(val email:String): AuthEvents()
    data class OnNameChange(val name:String): AuthEvents()
    data class OnPasswordChange(val password:String): AuthEvents()
    data class Login(val userData: UserData): AuthEvents()
    data class Signup(val userData : UserData ): AuthEvents()

}