package com.toufiq.mycomposeapplication.ui

import androidx.lifecycle.ViewModel

class FactsViewModel : ViewModel() {

    fun generateRandomFact(selectedAnimal: String): String {
        return if (selectedAnimal == "dog") {
            getDogFacts().random()
        } else {
            getCatFacts().random()
        }
    }

    fun getDogFacts(): List<String> {
        return listOf(
            "Dogs have a sense of time and can distinguish between short time intervals.",
            "The Basenji dog is known as the 'barkless dog' because it doesn't bark like other breeds.",
            "Dogs have a wet nose to help absorb scent chemicals.",
            "The Newfoundland breed has webbed feet, making them excellent swimmers.",
            "Dalmatian puppies are born completely white and develop their spots as they grow older.",
            "Dogs have an extraordinary sense of smell, estimated to be tens of thousands to 100,000 times more sensitive than humans.",
            "Dogs can learn to understand hundreds of words and commands.",
            "The Basenji dog is the only dog breed that cannot bark.",
            "The world's oldest known breed is the Saluki, originally bred in Egypt around 329 B.C.",
            "Dogs dream just like humans do, evidenced by their twitching and paw movements while asleep.",
            "The Norwegian Lundehund is a dog breed with six toes on each foot.",
            "Dogs have a sense of direction and can find their way home even if they're miles away.",
            "Dogs have unique nose prints, much like human fingerprints.",
            "The world's smallest dog breed is the Chihuahua.",
            "Dogs have three eyelids: one upper eyelid, one lower eyelid, and a third lid, called a nictitating membrane or 'haw,' which helps protect the eye."
        )
    }

    fun getCatFacts(): List<String> {
        return listOf(
            "Cats have five toes on their front paws, but only four toes on their back paws.",
            "The average cat sleeps for around 12-16 hours a day.",
            "Cats use their whiskers to measure gaps before attempting to go through them.",
            "A group of cats is called a 'clowder'.",
            "Cats can rotate their ears 180 degrees.",
            "Cats have a unique grooming pattern; they lick their fur to cool themselves down and promote blood flow.",
            "A cat's nose print is as unique as a human's fingerprint.",
            "Cats can make over 100 different sounds, while dogs make around 10.",
            "Adult cats don't meow at each other; they reserve this sound for communicating with humans.",
            "The world's oldest cat on record lived to be 38 years old.",
            "Cats have a specialized collarbone that allows them to always land on their feet when falling.",
            "Cats have a third eyelid called the 'haw' that helps protect their eyes.",
            "Cats have a strong aversion to citrus scents.",
            "Cats have a special reflective layer behind their retinas, which enhances their night vision.",
            "The technical term for a cat's hairball is 'bezoar'."
        )
    }
}