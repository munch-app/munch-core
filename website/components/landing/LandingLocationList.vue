<template>
  <div class="LandingLocationList Container">
    <div class="LocationList NoSelect">
      <div class="Elevation1 ElevationHover2 HoverPointer Border2 EachLocation" v-for="location in locations"
           :key="location.name">
        <div class="ImageBox Primary050Bg">
          <simple-svg fill="rgba(0, 0, 0, 0.75)" :filepath="`/img/landing/${location.image}`"/>
        </div>
        <div class="ContentBox">
          <div class="Name">
            {{location.name}}
          </div>
          <div class="Address Text">
            {{location.address}}
          </div>
        </div>
      </div>
      <div class="Elevation1 ElevationHover2 HoverPointer Border2 AddLocation EachLocation">
        <div class="ImageBox">
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
          {image: 'home.svg', name: 'Home', address: 'Bishan Street 55', description: ''},
          {image: 'work.svg', name: 'Work', address: '73 Ayer Rajah Crescent', description: ''},
        ]
      }
    }
  }
</script>

<style scoped lang="less">
  .LocationList {
    margin-top: 16px;
    margin-left: -15px;
    margin-right: -15px;
    padding-top: 2px;
    padding-bottom: 2px;

    display: flex;
    overflow-y: visible;
    overflow-x: scroll;
    -webkit-overflow-scrolling: touch;

    .EachLocation {
      display: flex;
      margin-right: 12px;
      margin-left: 12px;
      width: 240px;

      div.ImageBox {
        width: 64px;
        height: 64px;
        padding: 16px;
      }

      div.ContentBox {
        text-overflow: ellipsis;
        white-space: nowrap;
        overflow: hidden;

        .Name {
          font-weight: 600;
          font-size: 17px;
          line-height: 1.3;
          color: rgba(0, 0, 0, 0.75);
          padding-top: 8px;
          padding-left: 12px;
        }

        .Address {
          padding-left: 12px;
          padding-right: 18px;
          font-size: 15px;

          text-overflow: ellipsis;
          white-space: nowrap;
          overflow: hidden;
        }
      }
    }

    .AddLocation {
      width: initial;

      .Image {
        padding: 18px;
      }
    }
  }
</style>
