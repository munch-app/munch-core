<template>
  <div>
    <div class="LocationList NoSelect">
      <div class="Elevation1 ElevationHover2 HoverPointer Border2 EachLocation" v-for="location in locations"
           :key="location.name">
        <div class="Image Primary050Bg">
          <simple-svg fill="rgba(0, 0, 0, 0.75)" :filepath="`/img/landing/${location.image}`"/>
        </div>
        <div class="Name">
          {{location.name}}
        </div>
      </div>
      <div class="Elevation1 ElevationHover2 HoverPointer Border2 AddLocation">
        <div class="Image">
          <simple-svg fill="rgba(0, 0, 0, 0.75)" :filepath="`/img/landing/add.svg`"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "LandingLocationList",
    data() {
      return {
        locations: [
          {image: 'home.svg', name: 'Home', description: ''},
          {image: 'work.svg', name: 'Work', description: ''},
        ]
      }
    }
  }
</script>

<style scoped lang="less">
  .LocationList {
    display: flex;
    margin-top: 16px;

    .EachLocation {
      display: flex;
      margin-right: 32px;
      width: 240px;

      .Name {
        font-weight: 600;
        font-size: 17px;
        line-height: 1.5;
        color: rgba(0, 0, 0, 0.75);
        padding: 8px 14px;
      }
    }

    .Image {
      width: 64px;
      height: 64px;
      padding: 16px;
    }

    .AddLocation {
      .Image {
        padding: 18px;
      }
    }
  }
</style>
