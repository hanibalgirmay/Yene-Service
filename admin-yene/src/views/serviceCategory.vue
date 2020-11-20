<template>
  <section>
    details there are {{ serviceNumbers }} services available catagories
    <div class="cont">
      <md-card v-for="item in service" :key="item">
        <md-card-header>
          <div class="md-title">{{ item.name }}</div>
          <button class="btn">
            <md-icon class="icon">delete</md-icon>
          </button>
        </md-card-header>
        <md-card-content>{{ item.category }}</md-card-content>
        <md-card-media>
          <img class="img" :src="item.image" :alt="item.category" />
        </md-card-media>

        <md-card-actions>
          <md-button>Action</md-button>
        </md-card-actions>
      </md-card>
    </div>
  </section>
</template>

<script>
import { db } from "../firebase.config";

export default {
  props: ["serviceName"],
  data() {
    return {
      serviceNumbers: null,
      service: [],
    };
  },
  methods: {
    countService() {
      db.collection("Services_List")
        .where("category", "==", this.$props.serviceName.toLowerCase())
        .get()
        .then((res) => {
          this.serviceNumbers = res.docs.length;
          res.forEach((d) => {
            console.log(d.data());
            this.service.push(d.data());
            console.log("_", d);
          });
        });
    },
  },
  created() {
    // this.service = this.$route.params.serviceName;
    // if (this.service != null) {
    this.countService();
    // }
  },
};
</script>

<style scoped>
section {
  min-height: 100vh;
}
.img {
  width: 100px;
  height: 50px;
}
.cont {
  display: flex;
  width: 100%;
  flex-wrap: wrap;
}
.md-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 0;
  width: 100%;
  padding: 0;
}
.icon {
  margin: 0;
  /* display: flex; */
  text-align: end;
  /* justify-content: flex-end; */
  align-items: flex-end;
}
.btn {
  color: red;
  background-color: red;
  border: none;
}
</style>
