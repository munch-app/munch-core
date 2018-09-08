<template>
  <a class="InstagramMediaCard" :href="href" target="_blank" rel="nofollow">
    <image-size class="Image Elevation1 Border48 BorderImage" :image="media.image">
      <div class="ImageBox">
        <div class="Small UsernameButton Border24 Elevation1">@{{media.user.username}}</div>
      </div>
    </image-size>
  </a>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  export default {
    name: "InstagramMediaCard",
    components: {ImageSize},
    props: {
      media: {
        required: true,
        type: Object
      }
    },
    computed: {
      href() {
        return this.media.link
      }
    }
  }
</script>

<style scoped lang="less">
  .InstagramMediaCard {
    &:hover {
      cursor: pointer;
    }

    .Image {
      width: 100%;
      padding-top: 100%;

      position: relative;
    }

    .ImageBox {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;

      display: flex;
      justify-content: flex-end;
      align-items: flex-end;
    }

    .UsernameButton {
      background-color: rgba(255, 255, 255, 0.75);
      padding: 6px 10px;
      margin-right: 8px;
      margin-bottom: 8px;
    }
  }
</style>
