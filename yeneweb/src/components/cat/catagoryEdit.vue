<template>
    <div class="row justify-content-center">
        <div class="col-md-5">
            <h3 class="text-center">Update atagory</h3>
            <form @submit.prevent="onUpdateForm">
                <div class="form-group">
                    <label>Catagory Name</label>
                    <input type="text" class="form-control" v-model="catagory.name" required>
                </div>

                <div class="form-group">
                    <label>Catagory Discription</label>
                    <textarea rows="5" type="text" class="form-control" v-model="catagory.disc" required>
                    </textarea>
                </div>

                <div class="form-group">
                    <button class="btn btn-primary btn-block">Add Catagory</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
import db from "../../db.js";


    export default {
        data() {
            return {
                catagory: {
                }
            }
        },
        created() {
            let dbRef = db.collection('catagories').doc(this.$route.params.id);
            dbRef.get().then((doc) => {
                this.catagory = doc.data();
            }).catch((error) => {
                console.log(error)
            })
        },
        methods: {
            onUpdateForm(event) {
                event.preventDefault()
                db.collection('catagories').doc(this.$route.params.id)
                .update(this.catagory).then(() => {
                    console.log("Catagory successfully updated!");
                    this.$router.push('/clist')
                }).catch((error) => {
                    console.log(error);
                });
            }
        }
    }
</script>