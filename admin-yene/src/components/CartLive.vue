<template>
    <div>
        <graph-line-timerange
            :width="600"
            :height="400"
            :axis-min="0"
            :axis-max="100"
            :axis-reverse="false"
            :axis-format="'HH:mm'"
            :axis-interval="1000 * 60 * 60 * 8"
            :labels="labels"
            :values="values">
        <note :text="'Line Chart'"></note>
        <guideline :tooltip-x="true" :tooltip-y="true"></guideline>
    </graph-line-timerange>
    </div>
</template>

<script>
export default {
    name:"CartLive",
    data:()=>({
        labels: [ new Date("2018-07-07 00:00:00"), new Date("2018-07-08 00:00:00") ],
        values: [
            [ new Date("2018-07-07 00:00:00"), 55 ],
            [ new Date("2018-07-07 12:42:33"), 30 ],
            [ new Date("2018-07-07 15:11:12"), 70 ],
            [ new Date("2018-07-07 19:12:00"), 90 ],
            [ new Date("2018-07-07 22:55:55"), 45 ]
        ]
    })
}
</script>

<style>

</style>