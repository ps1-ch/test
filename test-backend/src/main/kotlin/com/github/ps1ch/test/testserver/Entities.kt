package com.github.ps1ch.test.testserver

import com.fasterxml.jackson.annotation.JsonBackReference
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
data class User(
    private val password: String? = null,
    @Id private val username: String? = null
) : UserDetails {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority("ROLE_USER"))
    override fun isEnabled() = true
    override fun getUsername() = username
    override fun isCredentialsNonExpired() = true
    override fun getPassword() = password
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true

    companion object {
        private const val serialVersionUID = 1L
    }
}

@Entity
class Account(
    var name: String,
    var color: Int,
    @OneToOne @JoinColumn(name = "inbound_id") @JsonBackReference(value = "inbound-account") var inbound: Inbound,
    @ManyToOne var user: User,
    @Id @GeneratedValue var id: Long? = null
)

@Entity
class Inbound(
    var host: String,
    var username: String,
    var password: String,
    @OneToOne(mappedBy = "inbound") @JsonBackReference(value = "account-inbound") var account: Account,
    @Id @GeneratedValue var id: Long? = null
)
