<template>
  <div class="page-container md-layout-column">
    <!-- <md-app>
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
          <md-list-item>
            <md-icon>dashboard</md-icon>
            <span class="md-list-item-text"
              ><router-link to="/">Dashboard</router-link></span
            >
          </md-list-item>
          <md-list-item>
            <md-icon>person</md-icon>
            <span class="md-list-item-text"
              ><router-link to="/users">Users</router-link></span
            >
          </md-list-item>

          <md-list-item>
            <md-icon>home_repair_service</md-icon>
            <span class="md-list-item-text"
              ><router-link to="/provider">Providers</router-link></span
            >
          </md-list-item>

          <md-list-item>
            <md-icon>report</md-icon>
            <span class="md-list-item-text"
              ><router-link to="/report">Report</router-link></span
            >
          </md-list-item>

          <md-list-item>
            <md-icon>payment</md-icon>
            <span class="md-list-item-text"
              ><router-link to="/payment">Payment</router-link></span
            >
          </md-list-item>

          <md-list-item>
            <md-icon>move_to_inbox</md-icon>
            <span class="md-list-item-text">Message</span>
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

      <md-app-content> -->
        <router-view />

        <!-- <Table /> -->
      <!-- </md-app-content>
    </md-app> -->
  </div>
</template>

<style lang="less">
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
  // width: 100%;
}
</style>
<script>
// import DrawGraph from "./DrawGraph";
// import CartLive from "./CartLive";
// import Table from "./Table";

export default {
  name: "App",
};
</script>
