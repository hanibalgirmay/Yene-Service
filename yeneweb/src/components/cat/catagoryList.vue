<template>

<div class="mt-3">
    <div class="container">
        </div>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Discription</th>
                        <th>Created</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="catagory in Catagories" :key="catagory.key">
                        <td>{{ catagory.name }}</td>
                        <td>{{ catagory.disc }}</td>
                        <td>{{ catagory.timestamp }}</td>
                        <td>
                            <router-link :to="{name: 'cedit', params: { id: catagory.key }}" class="btn btn-submit"><font-awesome-icon icon="list-ul"></font-awesome-icon>
                            </router-link>
                            
                            
                            <button @click.prevent="deleteCatagory(catagory.key)" class="btn btn-danger"><font-awesome-icon icon="trash"></font-awesome-icon></button>
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
                Catagories: []
            }
        },
        created() {
            db.collection('catagories').onSnapshot((snapshotChange) => {
                this.Catagories = [];
                snapshotChange.forEach((doc) => {
                    this.Catagories.push({
                        key: doc.id,
                        name: doc.data().name,
                        disc: doc.data().disc,
                        timestamp: doc.data().timestamp,

                    })
                });
            })
        },
        components: {
            FontAwesomeIcon
        },
        methods: {
            deleteCatagory(id){
              if (window.confirm("Do you really want to delete?")) {
                db.collection("catagories").doc(id).delete().then(() => {
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
