<template>

<div class="mt-3">
    <div class="container">
        </div>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Full Name</th>
                        <th>email</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="provider in providers" :key="provider.key">
                        <td>{{ provider.firstName }} </td>
                        <td>{{ provider.email }}</td>
                        <td>
                            <router-link :to="{name: 'pedit', params: { id: provider.key }}" class="btn btn-submit"><font-awesome-icon icon="list-ul"></font-awesome-icon>
                            </router-link>
                            
                            
                            <button @click.prevent="deleteCatagory(provider.key)" class="btn btn-danger"><font-awesome-icon icon="trash"></font-awesome-icon></button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    </div>

</template>
<script>
import db from "../../db.js";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

    export default {
        data() {
            return {
                providers: []
            }
        },
        created() {
            db.collection('serviceProviders').onSnapshot((snapshotChange) => {
                this.providers = [];
                snapshotChange.forEach((doc) => {
                    this.providers.push({
                        key: doc.id,
                        firstName: doc.data().firstName,
                        email: doc.data().email,

                    })
                });
            })
        },
        components: {
            FontAwesomeIcon
        },
        methods: {
            deleteProvider(id){
              if (window.confirm("Do you really want to delete?")) {
                db.collection("serviceProviders").doc(id).delete().then(() => {
                    console.log("Document deleted!");
                })
                .catch((error) => {
                    console.error(error);
                })
              }
            }
        }
    }
</script>

<style>
    .btn-primary {
        margin-right: 12px;
    }
</style>
