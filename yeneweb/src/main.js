import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";


import VueGeolocation from 'vue-browser-geolocation'
Vue.use(VueGeolocation);

import * as VueGoogleMaps from 'vue2-google-maps'
Vue.use(VueGoogleMaps, {
  load: {
    key: 'AIzaSyDovg5R6Z-5FkfhNgwer7pzBcuNJigbliw'
    

  },
  installComponents: false

})
import {library} from "@fortawesome/fontawesome-svg-core";


import {
  faTrash, faLink, faListUl,faRandom, faUndo, faStar, faEnvelope, faPlusCircle, faPlus
} from "@fortawesome/free-solid-svg-icons";


library.add(faTrash, faLink, faListUl,faRandom, faUndo, faStar, faEnvelope, faPlusCircle, faPlus);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
