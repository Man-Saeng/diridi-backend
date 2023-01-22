package com.mansaeng.diridibackend.entity.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class User(
    var id: Long,
    private var username: String,
    private var password: String,
    var enabled: Boolean,
    var roles: List<Role>
) : UserDetails {
    companion object {
        const val serialVersionUid: Long = 1L
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles.map { role -> SimpleGrantedAuthority(role.name) }
    }

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean = enabled
}
