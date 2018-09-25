<template>
  <place-card :place="card.place"/>
</template>

<script>
  import PlaceCard from "../../places/PlaceCard";

  export default {
    name: "SearchCardPlace",
    components: {PlaceCard},
    props: {
      card: {
        required: true,
        type: Object
      }
    },
  }
</script>
