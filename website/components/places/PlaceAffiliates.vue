<template>
  <div>
    <div>
      <h4 class="mb-8">Reserve with</h4>
    </div>
    <div class="flex-wrap m--4">
      <div class="p-4" v-for="affiliate in affiliates" :key="affiliate.uid">
        <div class="border border-3 overflow-hidden" @click="onClick(affiliate)">
          <cdn-img :image="affiliate.brand.image" type="320x320" object-fit="contain" height="48px" width="initial">
            <div class="hover-bg-a10 hover-pointer"/>
          </cdn-img>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import CdnImg from "../utils/image/CdnImg";

  function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
  }

  export default {
    name: "PlaceAffiliates",
    components: {CdnImg},
    props: {
      place: {
        type: Object,
        required: true
      }
    },
    computed: {
      affiliates() {
        const affiliates = [...this.place.affiliates]
        shuffleArray(affiliates)
        return affiliates;
      }
    },
    methods: {
      onClick(affiliate) {
        const url = affiliate.url
        const win = window.open(url, '_blank')
        win.focus()
      }
    }
  }
</script>
