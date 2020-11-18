import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import About from "../views/About.vue";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Providers from "../views/Providers.vue";
import Category from "../views/Category.vue";
Vue.use(VueRouter);

const routes = [
  {
    //Ctagory Start
    path: '/test',
    name: 'test',
    component: () => import('../components/cat/test')
  },
  //main start
  {
    path: "/home",
    name: "home",
    component: Home
  },
  {
    path: "/",
    name: "home",
    component: Home
  },
  {
    path: "/register",
    name: "register",
    component: Register
  },  
  {
    path: "/login",
    name: "login",
    component: Login
  },
  {
    path: "/providers",
    name: "providers",
    component: Providers
  },
  {
    path: "/category",
    name: "category",
    component: Category
  },
  {
    path: "*",
    redirect: "/"
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: About

    //Main End
  },
  {
    //Ctagory Start
    path: '/clist',
    name: 'clist',
    component: () => import('../components/cat/catagoryList')
  },
  {
    //Ctagory Start
    path: '/ccreate',
    name: 'ccreate',
    component: () => import('../components/cat/catagoryCreate')
  },
  {
    path: '/cedit/:id',
    name: 'cedit',
    component: () => import('../components/cat/catagoryEdit')
  },
  {
    path: '/pcreate',
    name: 'pcreate',
    component: () => import('../components/pro/providerCreate')
    //Catagory End
  },
  {
    //Ctagory Start
    path: '/plist',
    name: 'plist',
    component: () => import('../components/pro/providerList')
  },
  {
    path: '/pedit/:id',
    name: 'pedit',
    component: () => import('../components/pro/providerEdit')
        //Catagory End

  }
    //Catagory End
];

const router = new VueRouter({
  routes
});

export default router;
