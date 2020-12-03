<template>
  <div class="page-container md-layout-column">
    <md-app>
      <md-app-toolbar class="md-primary" md-fixed="true" md-elevation="0">
        <md-button
          class="md-icon-button"
          @click="toggleMenu"
          v-if="!menuVisible"
        >
          <md-icon>menu</md-icon>
        </md-button>
        <span class="md-title">My Title</span>

        <div class="md-toolbar-section-end">
          <md-avatar>
            <img
              src="https://www.w3schools.com/howto/img_avatar.png"
              alt="Avatar"
            />
          </md-avatar>
          <md-button @click="showSidepanel = true">Favorites</md-button>
        </div>
      </md-app-toolbar>

      <md-app-drawer :md-active.sync="menuVisible" md-persistent="mini">
        <md-toolbar class="md-transparent" md-elevation="0">
          <span>Navigation</span>

          <div class="md-toolbar-section-end">
            <md-button class="md-icon-button md-dense" @click="toggleMenu">
              <md-icon>keyboard_arrow_left</md-icon>
            </md-button>
          </div>
        </md-toolbar>

        <md-list>
          <md-list-item to="/">
            <md-icon>dashboard</md-icon>
            <span class="md-list-item-text">Dashboard</span>
          </md-list-item>
          <md-list-item to="/service-list">
            <md-icon>list</md-icon>
            <span class="md-list-item-text">Services</span>
          </md-list-item>
          <md-list-item to="/users">
            <md-icon>person</md-icon>
            <span class="md-list-item-text">Users</span>
          </md-list-item>

          <md-list-item to="/provider">
            <md-icon>home_repair_service</md-icon>
            <span class="md-list-item-text">Providers</span>
          </md-list-item>

          <md-list-item to="/report">
            <md-icon>report</md-icon>
            <span class="md-list-item-text">Report</span>
          </md-list-item>

          <md-list-item to="/reviews">
            <md-icon>start</md-icon>
            <span class="md-list-item-text">Reviews</span>
          </md-list-item>

          <md-list-item to="/payment">
            <md-icon>payment</md-icon>
            <span class="md-list-item-text">Payment</span>
          </md-list-item>

          <md-list-item>
            <md-icon>move_to_inbox</md-icon>
            <span class="md-list-item-text">Message</span>
          </md-list-item>

          <md-list-item @click="logout">
            <md-icon>move_to_inbox</md-icon>
            <span class="md-list-item-text">Log out</span>
          </md-list-item>
        </md-list>
      </md-app-drawer>

      <md-drawer class="md-right" :md-active.sync="showSidepanel">
        <md-avatar>
          <img
            src="https://www.w3schools.com/howto/img_avatar.png"
            alt="Avatar"
          />
        </md-avatar>
        <md-toolbar class="md-transparent" md-elevation="0">
          <span class="md-title">Favorites</span>
        </md-toolbar>

        <md-list>
          <md-list-item>
            <span class="md-list-item-text">Abbey Christansen</span>

            <md-button class="md-icon-button md-list-action">
              <md-icon class="md-primary">chat_bubble</md-icon>
            </md-button>
          </md-list-item>

          <md-list-item>
            <span class="md-list-item-text">Alex Nelson</span>

            <md-button class="md-icon-button md-list-action">
              <md-icon class="md-primary">chat_bubble</md-icon>
            </md-button>
          </md-list-item>

          <md-list-item>
            <span class="md-list-item-text">Mary Johnson</span>

            <md-button class="md-icon-button md-list-action">
              <md-icon>chat_bubble</md-icon>
            </md-button>
          </md-list-item>
        </md-list>
      </md-drawer>

      <md-app-content>
        <router-view />

        <!-- <Table /> -->
      </md-app-content>
    </md-app>
  </div>
</template>

<script>
import { auth } from "./firebase.config";

export default {
  name: "Home",
  data: () => ({
    menuVisible: false,
    showNavigation: false,
    showSidepanel: false,
  }),
  methods: {
    toggleMenu() {
      this.menuVisible = !this.menuVisible;
    },
    logout() {
      auth.signOut();
      localStorage.removeItem("adminKey");
      this.$router.replace({ name: "/" });
    },
  },
};
</script>

<style>
.page-container {
  height: 100%;
  min-height: 100vh;
  widows: 100%;
  overflow: hidden;
  position: relative;
  border: 1px solid rgba(#000, 0.12);
}
.md-drawer {
  width: 230px;
  height: 100vh;
  max-width: calc(100vw - 125px);
}
.md-card {
  width: 320px;
  margin: 4px;
  display: inline-block;
  vertical-align: top;
}
.md-content {
  padding: 16px;
}
</style>
