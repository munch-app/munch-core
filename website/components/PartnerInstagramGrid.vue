<template>
  <div>
    <div class="InstagramImage container">
      <b-row no-gutters>
        <b-col cols="4" md="3" v-for="media in parsed" :key="media.mediaId">
          <a target="_blank" :href="media.url" rel="noopener noreferrer nofollow">
            <b-img :src="media.image" fluid/>
          </a>
        </b-col>
      </b-row>
    </div>
  </div>
</template>

<script>
  export default {
    props: ['medias'],
    computed: {
      parsed: function () {
        return [{
          "url": "https://www.instagram.com/p/BmYLYMAn1rq/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_1844274088082627306_6086699996_640x640.jpg",
        }, {
          "url": "https://www.instagram.com/p/qDjFEzHTRW/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_757603442706166870_2401868_640x640.jpg",
        }, {
          "url": "https://www.instagram.com/crystal_wee/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_yfy1uPkBAigzaRrcoqCDOYGi7Qa_EJoUwovJ9rsbyeQ_320x320..jpg",
        }, {
          "url": "https://www.instagram.com/p/BDx-a1NJmv5/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_1221031496713137145_1102457_640x640.jpg",
        }, {
          "url": "https://www.instagram.com/p/Bi38D4fHMjw/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_1781156327248939248_1102457_640x800.jpg",
        }, {
          "url": "https://www.instagram.com/p/hAjilfMKPn/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_594631459154732007_565548111_640x640.jpg",
        }, {
          "url": "https://www.instagram.com/p/yUYHGpntvw/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_906455466301643760_207871973_640x640.jpg",
        }, {
          "url": "https://www.instagram.com/mightyfoodie/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_40QOsb7gSh_LHs3OslHjtvVSVgcRy-TzJ5sUZ3jUmJo_320x320..jpg",
        }, {
          "url": "https://www.instagram.com/wishuponatart/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_o3qEZHFaYBbOR3CHo0o4ZJbWQ8xY1fssa_d8rKiriZw_320x400..jpg",
        }, {
          "url": "https://www.instagram.com/p/BVen2p_lCz6/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_1539843402363055354_565548111_640x640.jpg",
        }, {
          "url": "https://www.instagram.com/p/BhWHZBoHaYi/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_1753621610966656546_27077575_640x359.jpg",
        }, {
          "url": "https://www.instagram.com/p/BiWB3H4lJE8/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_1771611689500053820_565548111_640x640.jpg",
        }]
      }
    }
  }
</script>

<style>
  .InstagramImage a img {
    object-fit: cover;
    width: 320px;
    height: 230px;
    padding: 8px;
  }

  @media screen and (max-width: 992px) {
    .InstagramImage a img {
      object-fit: cover;
      width: 200px;
      height: 130px;
    }
  }

  @media screen and (max-width: 500px) {
    .InstagramImage a img {
      object-fit: cover;
      width: 160px;
      height: 100px;
    }
  }
</style>
