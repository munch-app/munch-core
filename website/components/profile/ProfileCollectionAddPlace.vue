<template>
  <portal to="dialog">
    <h3>Add To Collection</h3>
    <div class="ListView">
      <div class="">
        <div class="Collection flex" v-for="collection in list" :key="collection.collectionId">
          <image-size class="Image" :image="collection.image"/>
          <div class="Content">
            <div class="Name text">{{collection.name}}</div>
            <div class="small-bold">{{collection.count}} places</div>
          </div>
        </div>
      </div>
      <div class="LoadingIndicator" v-if="more" v-observe-visibility="{callback:visibilityChanged,throttle:500}">
        <beat-loader color="#084E69" size="14px"/>
      </div>
    </div>
    <button class="clear-elevated hover-pointer" @click="$emit('on-close')">Close</button>
  </portal>
</template>

<script>
  import {mapGetters} from 'vuex'
  import BeatLoader from "vue-spinner/src/BeatLoader";
  import ImageSize from "../core/ImageSize";

  export default {
    name: "ProfileCollectionAddPlace",
    components: {ImageSize, BeatLoader},
    props: {
      place: {
        type: Object,
        required: true
      }
    },
    computed: {
      ...mapGetters('user/collections', ['list', 'more']),
    },
    methods: {
      visibilityChanged(isVisible) {
        if (isVisible) {
          this.$store.dispatch('user/collections/load')
        }
      },
    }
  }
</script>

<style scoped lang="less">
  h3 {
    padding-bottom: 12px;
  }

  button {
    float: right;
    margin-top: 16px;
  }

  .ListView {
    display: flex;
    flex-direction: column;

    margin-top: -16px;
    margin-bottom: -16px;

    max-height: 400px;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    .Collection {
      margin-top: 16px;
      margin-bottom: 16px;

      .Image {
        width: 50px;
        height: 50px;
        margin-right: 16px;
      }

      .Content {
        display: flex;
        flex-direction: column;
        justify-content: center;

        .Name {
          margin-bottom: -2px;
        }
      }
    }
  }
</style>
