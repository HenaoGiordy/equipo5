package com.univalle.equipo5.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.univalle.equipo5.model.UserRequest
import com.univalle.equipo5.model.UserResponse
import com.univalle.equipo5.repository.LoginRepository

class LoginViewModel : ViewModel(){
    private val repository = LoginRepository()
    private val _isRegister = MutableLiveData<UserResponse>()
    private val _isLogin = MutableLiveData<UserResponse>()
    val isLogin: LiveData<UserResponse> = _isLogin
    val isRegister: LiveData<UserResponse> = _isRegister

    fun registerUser(userRequest: UserRequest) {
        repository.registerUser(userRequest) { userResponse ->
            _isRegister.value = userResponse
        }
    }

    fun loginUser(email: String, password: String) {
        repository.loginUser(email, password) { isSuccessful ->
            val response = if (isSuccessful) {
                UserResponse(
                    email = email,
                    isRegister = false, // No es registro
                    message = "Inicio de sesión exitoso"
                )
            } else {
                UserResponse(
                    isRegister = false, // No es registro
                    message = "Error en el inicio de sesión"
                )
            }
            _isLogin.value = response
        }
    }

    fun sesion(email: String?, isEnableView: (Boolean) -> Unit) {
        if (email != null) {
            isEnableView(true)
        } else {
            isEnableView(false)
        }
    }

}