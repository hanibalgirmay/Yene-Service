<template>
  <div id="app">
    <Navigation :user="user" @logout="logout"/>
    <router-view 
      class="container " 
      :user="user"
      @logout="logout"
      />
  </div>
</template>
<script>
import Navigation from "@/components/Navigation.vue";
// import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

import Firebase from "firebase";
import db from "./db.js";
export default {
  name: "App",
  data: function() {
    return {
      user: null
    };
  },
    methods: {
    logout: function(){
      Firebase.auth()
      .signOut()
      .then( () => {
        this.user = null;
        this.$router.push("login");
      });
    },
  },
  mounted() {
    Firebase.auth().onAuthStateChanged(user => { 
      if (user) {
        this.user = user.email;
      }
      })

    db.collection("users")
      .doc("2EJUVMe1SgWTeEQCiC2C")
      .get()
      .then(snapshot => {
        this.user = snapshot.data().name;
      });
  },
  components: {
    Navigation
  }
};
</script>


<style lang="scss">
$primary: #05b2dd;
@import "node_modules/bootstrap/scss/bootstrap";
</style>