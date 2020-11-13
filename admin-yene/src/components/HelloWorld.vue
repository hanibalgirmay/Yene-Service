<template>
  <div class="hello">
    <div class="md-layout">
      <div class="md-layout-ite">
        <md-card md-with-hover class="md-primary">
          <md-card-header>
            <div class="md-title">Total Users</div>
          </md-card-header>

          <md-card-content>
            <p class="content">{{ countUser }}</p>
          </md-card-content>

          <md-card-actions>
            <md-button>Graph</md-button>
            <router-link to="/users"><md-button>View</md-button></router-link>
          </md-card-actions>
        </md-card>
      </div>
      <div class="md-layout-ite">
        <md-card md-with-hover class="md-primary" md-theme="orange-card">
          <md-card-header>
            <div class="md-title">Registerd Providers</div>
          </md-card-header>

          <md-card-content>
            <p class="content">{{ countProvider }}</p>
          </md-card-content>

          <md-card-actions>
            <md-button>Graph</md-button>
            <router-link to="/provider"
              ><md-button>View</md-button></router-link
            >
          </md-card-actions>
        </md-card>
      </div>
      <div class="md-layout-ite">
        <md-card md-with-hover class="md-accent">
          <md-card-header>
            <div class="md-title">Reports</div>
          </md-card-header>

          <md-card-content>
            <p class="content">{{ countReport }}</p>
          </md-card-content>

          <md-card-actions>
            <md-button>View</md-button>
          </md-card-actions>
        </md-card>
      </div>
    </div>
    <!-- <div class="md-layout">
      <div class="md-layout-item">
        <DrawGraph />
      </div>
      <div class="md-layout-item">
        <CartLive />
      </div>
    </div> -->
  </div>
</template>

<script>
import {
  usersCollection,
  reportsCollection,
  providerCollection,
} from "../firebase.config";

export default {
  name: "HelloWorld",
  props: {
    msg: String,
  },
  data: () => ({
    countUser: null,
    countProvider: null,
    countReport: null,
  }),
  methods: {
    countUsers() {
      usersCollection
        .get()
        .then((querySnapShot) => {
          this.countUser = querySnapShot.docs.length;
          // this.countUser = res;
          console.log("total users : ", this.countUser);
        })
        .catch((error) => console.log(error));
    },
    countProviders() {
      providerCollection
        .get()
        .then((querySnapShot) => {
          this.countProvider = querySnapShot.docs.length;
          console.log("total provider : ", this.countProvider);
        })
        .catch((error) => console.log(error));
    },
    countReports() {
      reportsCollection
        .get()
        .then((querySnapShot) => {
          this.countReport = querySnapShot.docs.length;
        })
        .catch((err) => console.error(err));
    },
  },

  mounted() {
    this.countProviders();
    this.countUsers();
    this.countReports();
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
.content {
  text-align: center;
  font-size: 2em;
  color: red;
}
</style>
