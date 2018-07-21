<template>
  <a class="InstagramMediaCard" :href="'https://instagram.com/' + media.username" target="_blank" rel="nofollow">
    <image-size class="Elevation1 Border48" :size-urls="media.images" background>
      <div class="Container">
        <div class="Content">
          <div class="Small SourceButton Border24 Elevation1">@{{media.username}}</div>
        </div>
      </div>
    </image-size>
  </a>
</template>

<script>
  import ImageSize from "../core/ImageSize";

  export default {
    name: "InstagramMediaCard",
    props: ['media'],
    components: {ImageSize}
  }
</script>

<style scoped lang="less">
  .InstagramMediaCard {
    .Container {
      width: 100%;
      padding-bottom: 100%;
    }

    .Content {
      position: absolute;
      width: 100%;
      height: 100%;

      display: flex;
      justify-content: flex-start;
      align-items: flex-end;

      .SourceButton {
        background-color: rgba(255, 255, 255, 0.75);
        padding: 6px 10px;
        margin-left: 8px;
        margin-bottom: 8px;
      }
    }
  }
</style>
