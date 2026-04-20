package com.example.filmsearch.data

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
}