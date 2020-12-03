import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";

Vue.use(VueRouter);

const routes = [
  // {
  //   path: "/Dashboard",
  //   name: "Home",
  //   component: Home,
  // },
  {
    path: "/dash",
    name: "Dashboard",
    component: () => import("../Home.vue"),
    children: [
      {
        path: "/",
        name: "Home",
        component: Home,
      },
      {
        path: "/provider",
        name: "Providers",
        component: () => import("../views/Provider.vue"),
      },
      {
        path: "/service-list",
        name: "servicesList",
        component: () => import("../views/ServiceList.vue"),
      },
      {
        path: "/service-category/:serviceName",
        name: "serviceCatgory",
        props: true,
        component: () => import("../views/serviceCategory.vue"),
      },
      {
        path: "/provider/add",
        name: "Add Provider",
        component: () => import("../views/AddProvider.vue"),
      },
      {
        path: "/payment",
        name: "Payment",
        component: () => import("../views/Payment.vue"),
      },
      {
        path: "/detail-user",
        name: "user-detail",
        props: true,
        component: () => import("../views/detail-user.vue"),
      },
      {
        path: "/reviews",
        name: "review",
        component: () => import("../views/Reviews.vue"),
      },
      {
        path: "/report",
        name: "Report",
        component: () => import("../views/Report.vue"),
      },
      {
        path: "/users",
        name: "Users",
        component: () => import("../views/Users.vue"),
      },
    ],
  },
  {
    path: "/",
    name: "Login",
    component: () => import("../views/Login.vue"),
  },
  {
    path: "/forgot-password",
    name: "Forgot Password",
    component: () => import("../views/Forgot-Password.vue"),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
