<template>
    <div>
        <h3>
            {{coordinates.lat}} Latitude, {{coordinates.lng}} longitude,
        </h3>
        <GmapMap
            :center="{lat:10, lng:10}"
            :zoom="7"
            style="width:640px; height:340;"

        >
        </GmapMap>


    </div>

</template>

<script>

export default {
    data () {
        return {
            coordinates: {
                lat: 0,
                lng: 0
            }
        }
    },
    created() {
        this.$getLocation({ })
        .then(coordinates => {
            this.coordinates = coordinates;
        })
        .catch(error => alert(error));
    }
}
</script>