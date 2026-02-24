package com.example.produtosdelimpeza.store.onboarding

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.model.BusinessHours
import com.example.produtosdelimpeza.core.domain.model.DayOfWeek
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.core.presentation.FieldState
import com.example.produtosdelimpeza.core.validation.EmailValidator
import com.example.produtosdelimpeza.core.validation.NameValidator
import com.example.produtosdelimpeza.store.onboarding.domain.SignUpStoreFormState
import com.example.produtosdelimpeza.store.onboarding.domain.SignUpStoreUiState
import com.example.produtosdelimpeza.store.onboarding.domain.SignupStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpStoreViewModel @Inject constructor(
    private val signupStoreRepository: SignupStoreRepository,
) : ViewModel() {

    private var _weeklySchedule = MutableStateFlow(
        DayOfWeek.entries.associateWith {
            BusinessHours(
                openTime = "",
                closeTime = "",
            )
        }
    )
    val weeklySchedule = _weeklySchedule.asStateFlow()


    private val _uiState = MutableStateFlow(SignUpStoreUiState())
    val uiState: StateFlow<SignUpStoreUiState> = _uiState.asStateFlow()

    var formState by mutableStateOf(SignUpStoreFormState())

    private val _isButtonValid = MutableStateFlow(false)
    val isButtonValid = _isButtonValid.asStateFlow()

    private val _isScheduleValid = MutableStateFlow(false)
    val isScheduleValid = _isScheduleValid.asStateFlow()


    fun updateDaySchedule(day: DayOfWeek, hours: BusinessHours) {
        _weeklySchedule.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[day] = hours
            }
        }

        validateSchedule()
    }


    fun updateAllDaysSchedule(hours: BusinessHours) {
        _weeklySchedule.update { currentMap ->
            currentMap.mapValues { entry ->
                entry.value.copy(
                    openTime = hours.openTime,
                    closeTime = hours.closeTime
                )
            }
        }
        _weeklySchedule.update { currentMap ->
            currentMap.mapValues {
                hours.copy(
                    openTime = hours.openTime,
                    closeTime = hours.closeTime
                )
            }
        }

        Log.d("vald", "kljndf: ${_weeklySchedule.value.values}")
        validateSchedule()
    }

    fun validateSchedule() {
        _isScheduleValid.update { _weeklySchedule.value.all { it.value.openTime.isNotBlank() && it.value.closeTime.isNotBlank() } }
    }


    fun save() {
        Log.d("TEste", "chegou aqui vuew")
    }


    fun updateName(name: String) {
        val isNameValid = NameValidator.isNameValid(name)

        formState = formState.copy(
                storeName = FieldState(
                    field = name,
                    error = isNameValid,
                    isValid = isNameValid == null,
                )
            )

        updateConfirmationButton()
    }


    fun updateEmail(email: String) {
        val isEmailValid = EmailValidator.validate(email)
        formState = formState.copy(
                email = FieldState(
                    field = email,
                    error = isEmailValid,
                    isValid = isEmailValid == null
                )
            )

        updateConfirmationButton()
    }


    fun updateDescription(description: String) {
        formState = formState.copy(
            description = FieldState(
                field = description,
                isValid = true,
                error = null
            )
        )
    }


    fun updateCategory(category: String) {
        formState = formState.copy(
            category = FieldState(
                field = category,
                isValid = true,
                error = null
            )
        )
    }


/*
    fun updatePhone(phone: String) {
        val isPhoneValid = PhoneValidator.isValid(phone)
        formState = formState.copy(
                phone = FieldState(
                    field = phone,
                    isValid = isPhoneValid,
                    error = if (!isPhoneValid) "Telefone inválido" else null
                )
            )

        updateConfirmationButton()
    }
*/

    fun updateConfirmationButton() {
        val isButtonValid = with(formState) {
            storeName.isValid
            && email.isValid
        }

        _isButtonValid.value = isButtonValid
    }



    fun createStore() {
        _uiState.update { it.copy(isLoading = true) }

        formState = formState.copy()
        val store = Store(
            name = formState.storeName.field,
            email = formState.email.field,
            phone = formState.phone.field,
            address = "Rua dos Bobos, 0",
            storeOperationTime = _weeklySchedule.value.mapKeys { it.key.name },
            description = formState.description.field,
            category = formState.category.field
        )
        viewModelScope.launch{
            val result = signupStoreRepository.createStore(store)

            if (result.isSuccess) _uiState.update { it.copy(goToHome = true) }
            else _uiState.update { it.copy(error = "Erro ao criar loja") }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}