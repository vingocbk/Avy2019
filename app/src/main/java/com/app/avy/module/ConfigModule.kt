package com.app.avy.module

data class ConfigModule(var all: List<All>)

data class All(var name: String, var models: List<String>)