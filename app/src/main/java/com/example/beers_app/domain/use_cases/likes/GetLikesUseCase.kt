package com.example.beers_app.domain.use_cases.likes

import com.example.beers_app.data.settings.AppSettings
import com.example.beers_app.domain.repositories.LikesRepository

class GetLikesUseCase(
    private val appSettings: AppSettings,
    private val userRepository: LikesRepository
) {
    suspend operator fun invoke(): List<String> {
        val userNumber = appSettings.getCurrentUser().phoneNumber
        if (userNumber != "") {
            return userRepository.getUserLikes(userNumber).menuItems.map { it.UID }
        }
        return emptyList()
    }
}