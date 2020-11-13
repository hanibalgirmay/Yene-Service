import Vue from "vue";
import App from "./App.vue";
import VueMaterial from "vue-material";
import "vue-material/dist/vue-material.min.css";
import "vue-material/dist/theme/default-dark.css"; // This line here
import { auth } from "./firebase.config";
import Vuelidate  from 'vuelidate'
import VueGeolocation from 'vue-browser-geolocation';
import * as VueGoogleMaps from 'vue2-google-maps'

//graph
import VueGraph from "vue-graph";

import router from "./router";

Vue.config.productionTip = false;
Vue.use(VueMaterial);
Vue.use(VueGraph);
Vue.use(VueGeolocation);
Vue.use(Vuelidate );
Vue.use(VueGoogleMaps,{
  load:{
    key:"AIzaSyBOczmolSP8zWCxESpPC_diT94kndQGoJU",
  }
})

let app;
auth.onAuthStateChanged(() => {
  if (!app) {
    app = new Vue({
      router,
      render: (h) => h(App),
    }).$mount("#app");
  }
});
