package com.univalle.equipo5.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.univalle.equipo5.model.Challenge
import com.univalle.equipo5.repository.ChallengeRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ChallengeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ChallengeViewModel
    private val challengeRepository: ChallengeRepository = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { challengeRepository.getAllChallenges(any()) } answers {
            val callback = it.invocation.args[0] as (List<Challenge>) -> Unit
            callback(emptyList())
        }

        viewModel = ChallengeViewModel(challengeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInsertChallenge() = runTest {
        val challenge = Challenge()
        coEvery { challengeRepository.insertChallenge(challenge) } returns "docId"

        viewModel.insertChallenge(challenge)
        advanceUntilIdle()

        Assert.assertEquals("docId", challenge.id)
        coVerify { challengeRepository.insertChallenge(challenge) }
    }

    @Test
    fun testInsertChallengeFailure() = runTest {
        val challenge = Challenge()
        coEvery { challengeRepository.insertChallenge(challenge) } throws Exception("Insert failed")

        viewModel.insertChallenge(challenge)
        advanceUntilIdle()

        Assert.assertNull(challenge.id)
        coVerify { challengeRepository.insertChallenge(challenge) }
    }

    @Test
    fun testUpdateChallengeFailure() = runTest {
        val challenge = Challenge()
        coEvery { challengeRepository.updateChallenge(challenge) } throws Exception("Update failed")

        viewModel.updateChallenge(challenge)
        advanceUntilIdle()

        coVerify { challengeRepository.updateChallenge(challenge) }
    }

    @Test
    fun testGetRandomChallengeFailure() = runTest {
        coEvery { challengeRepository.getRandomChallenge() } throws Exception("Random fetch failed")

        var result: Challenge? = null
        viewModel.getRandomChallenge {
            result = it
        }
        advanceUntilIdle()

        Assert.assertNull(result)
        coVerify { challengeRepository.getRandomChallenge() }
    }

    @Test
    fun testInitialFetchChallenges() {
        val observer: Observer<List<Challenge>> = mockk(relaxed = true)
        viewModel.challenges.observeForever(observer)

        verify { observer.onChanged(emptyList()) }
    }


    @Test
    fun testFetchChallenges() {
        val challengesList = listOf(Challenge(), Challenge())
        val observer: Observer<List<Challenge>> = mockk(relaxed = true)

        every { challengeRepository.getAllChallenges(any()) } answers {
            val callback = it.invocation.args[0] as (List<Challenge>) -> Unit
            callback(challengesList)
        }

        viewModel = ChallengeViewModel(challengeRepository)

        viewModel.challenges.observeForever(observer)

        verify { observer.onChanged(challengesList) }
    }
}