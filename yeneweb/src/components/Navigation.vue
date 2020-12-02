<template>
  <div>
    <nav class="site-nav navbar navbar-expand bg-primary navbar-dark">
      <div class="container-fluid">
        <router-link class="navbar-brand" to="/">Yene service</router-link>
        <div class="navbar-nav ml-auto">
          <router-link class="nav-item nav-link" to="/home">home</router-link>
          <router-link class="nav-item nav-link" to="/about">about</router-link>
          <router-link class="nav-item nav-link" to="/providers" v-if="user">Providers</router-link>
          <router-link class="nav-item nav-link" to="/category" v-if="user">Category</router-link>
          <router-link class="nav-item nav-link" to="/login" v-if="!user">log in</router-link>
          <router-link class="nav-item nav-link" to="/register" v-if="!user">register</router-link>
          <button class="nav-item nav-link btn" @click="$emit('logout')">Log Out</button>
        </div>

      </div>
    </nav>
  </div>
</template>
<script>
export default {
  name: "navigation",
  props: ["user"]
}
</script>