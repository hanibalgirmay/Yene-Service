<template>
    <div class="mt-3">
    <form @submit.prevent="onUpdateForm">
        <div class="row justify-content-center">
          <div class="col-lg-12">
            <div class="card bg-light">
              <div class="card-body">
                <h3 class="font-weight-light mb-3">Update Provider Detail</h3>

                <div class="form-row">
                  <section class="col-sm-4 form-group">
                    <input
                    class="form-control"
                    type="text"
                    id="firstName"
                    placeholder="First Name"
                    required
                    name="firstName"
                    v-model="provider.firstName"
                    />
                  </section>
                  <section class="col-sm-4 form-group">
                    <input
                      class="form-control"
                      id="lastName"
                      type="text"
                      required
                      name="lastName"
                      placeholder="Last Name"
                      v-model="provider.lastName"
                    />
                  </section>

                      <section class="col-sm-4 form-group">
                    <input
                      class="form-control"
                      id="phoneNumber"
                      type="number"
                      required
                      name="phoneNumber"
                      placeholder="Phone Number"
                      v-model="provider.phoneNumber"
                    />
                  </section>

                    <section class="col-sm-4 form-group">
                    <input
                      class="form-control"
                      id="email"
                      type="email"
                      required
                      name="email"
                      placeholder="Email"
                      v-model="provider.email"
                    />
                  </section>

                  <section class="col-sm-4 form-group">
                    <input
                      class="form-control"
                      id="city"
                      type="text"
                      required
                      name="city"
                      placeholder="City"
                      v-model="provider.city"
                    />
                  </section>

                    <section class="col-sm-4 form-group">
                    <select
                      class="form-control"
                      id="educationLevel"
                      type="text"
                      required
                      name="educationLevel"
                      placeholder="educationLevel"
                      v-model="provider.educationLevel"
                    >
                      <option selected="selected" data-default disabled >Select Your Education Level</option>
                      <option value="volvo">..............</option>
                      <option value="saab">...............</option>
                      <option value="opel">................</option>
                      <option value="audi">.................</option>
                    </select>
                  </section> 
                </div>
                
                <div class="form-row">
                  <section class="col-sm-4 form-group">
                    <select
                      class="form-control"
                      id="experience"
                      type="text"
                      required
                      name="experience"
                      placeholder="Experience"
                      v-model="provider.experience"
                    >
                    <option selected="selected" data-default disabled >Select Your expriance Level</option>
                      <option value="volvo">..............</option>
                      <option value="saab">...............</option>
                      <option value="opel">................</option>
                      <option value="audi">.................</option>
                  </select>
                  </section>

                    <br>
                                    <section class="col-sm-4 form-group">
                    <label>
                    <input type="radio" value="Male" v-model="provider.gender" />
                    Male
                  </label>
                  <label>
                    <input type="radio" value="Femal" v-model="provider.gender" />
                    Femal
                  </label>

    
                  </section>
                </div>


                <div class="form-row">


                                    <section class="col-sm-6 form-group">
                    <textarea
                      class="form-control"
                      rows="5"
                      id="aboutMe"
                      type="text"
                      required
                      name="aboutMe"
                      placeholder="Service Discription"
                      v-model="provider.aboutMe"
                    />
                  </section>
                  
                 <div class="form-row">
                  <section class="col-sm-12 form-group">
                    <label>Latitude : {{coordinates.lat}}</label>
                    <br>
                    <label>longitude : {{coordinates.lng}}</label>
                  </section>
                </div>

                </div>


                <div class="form-group text-right mb-0">
                  <button
                    class="btn btn-primary"
                    type="submit"
                  >
                    Update
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        
    </form>
    <p class="text-center mt-2">
      or
      <router-link to="/login">login</router-link>
    </p>
  </div>
</template>


<script>
import db from "../../db.js";


    export default {
        data() {
            return {
                coordinates: {
                    lat: 0,
                    lng: 0
                },
                provider: {
                }
            }
        },
        created() {

            
            let dbRef = db.collection('serviceProviders').doc(this.$route.params.id);
            dbRef.get().then((doc) => {
                this.provider = doc.data();
            }).catch((error) => {
                console.log(error)
            }),
            this.$getLocation({ })
            .then(coordinates => {
                this.coordinates = coordinates;
            })
            .catch(error => alert(error));
        },
        methods: {
            onUpdateForm(event) {
                event.preventDefault()
                db.collection('serviceProviders').doc(this.$route.params.id)
                .update(this.provider).then(() => {
                    console.log("provider successfully updated!");
                    this.$router.push('/plist')
                }).catch((error) => {
                    console.log(error);
                });
            }
        }
    }
</script>