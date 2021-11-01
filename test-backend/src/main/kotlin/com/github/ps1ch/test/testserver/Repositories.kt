package com.github.ps1ch.test.testserver

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String> {
    fun findByUsername(username: String): User?
}

interface AccountRepository : CrudRepository<Account, Long> {
    fun findAllByUser(user: User): Iterable<Account>
    fun findByUserAndName(user: User, name: String): Account?
}
