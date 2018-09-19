<template>
  <div>
    <div class="InstagramImage Container">
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
          "url": "https://www.instagram.com/makanranger/",
          "image": "https://scontent.cdninstagram.com/vp/a434c4fd839dc440ba096bb814d97054/5BCA09E3/t51.2885-15/e35/s320x320/37010660_275495839867915_6248278354470895616_n.jpg",
          "caption": "Plans to travel to Hokkaido this year fell through a few months ago, so i was really excited to try out Suage, a soup curry joint from Hokkaido which recently opened in Singapore\n\nSoup curry is a special delicacy which originated in Sapporo comprising meat, vegetables and curry-flavoured soup (as you could probably tell from its title haha). I had the Pork kakuni bowl ($13.80++) which came with stewed pork belly and some assorted vegetables. Sadly, my spice tolerance is very low so i chose level 1 spiciness...but the waiter did mention that you could ask for extra spice to be added on the spot if you feel you can handle a higher level of spiciness while trying the dish\n\nI liked the soup curry which felt really light with a not too overpowering hint of spiciness. Actually, the stewed pork belly stole the show for me because it was really tender with barely any hint of dryness. I would definitely recommend you try this pork belly too\n\nProtip: you can get this at 50% via the eatigo app\n.\n.\n.\n.\n.\n#sgfood #sgigfood #exploreflavours #forkyeah #f52grams #foodporn #foreverhungry #sgfoodie #foodgasm #sgcafe #sgeats #eatbooksg #nom #buzzfeast #sgfoodphotography #foodiegram #onthetableproject #sgfoodstagram #dailyfoodfeed #getinmybelly #eatingfortheinsta #eeeeeats #8dayseat #huffposttaste #burpple #foodbeast #foodoftheday #yum #japanesefood #stfoodtrending"
        }, {
          "url": "https://www.instagram.com/foodmakescalhappy/",
          "image": "https://scontent.cdninstagram.com/vp/a2280b5c3d4600d09f5192f89a5dd32e/5B9122DF/t51.2885-15/e35/p320x320/32979921_218566035404862_4314482819939172352_n.jpg",
          "caption": "Chirashi kou @chikuwatei 🇯🇵🍚 Sashimi with sweet prawns, unagi, scallop & ikura on a bed of Japanese rice 🐟🦐 It looks pretty. But the sashimi doesn't taste very fresh & the unagi is not really flavourful 😕 📍9 Mohamed Sultan Rd, Singapore 238959"
        }, {
          "url": "https://www.instagram.com/crystal_wee/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_yfy1uPkBAigzaRrcoqCDOYGi7Qa_EJoUwovJ9rsbyeQ_320x320..jpg",
        }, {
          "url": "https://www.instagram.com/mightyfoodie/",
          "image": "https://scontent.cdninstagram.com/vp/d45d79f3de2bde89d750884b663dc931/5B8ACE0E/t51.2885-15/s320x320/e35/24845407_1950764831840073_2551086453564112896_n.jpg",
          "caption": "[Christmas @ Little House of Dreams]\n.\n3 Course Christmas Menu at $69.90++ per pax which includes a Starter, Main and Dessert is available from 1 to 31 December 2017.\n.\nStarter:\n🍽 Truffle Ravioli with cheese & olive oil.\nOR\n🍽 Pan Seared Foie Gras drizzled with balsamic reduction, berries compote and caramelised apple.\n.\nMain:\n🍽 Lamb Shank that is braised over 6 hours till tender in red wine sauce.\nOR\n🍽 Poached Salmon in cream sauce, served on a bed of spinach and mash\n.\nDessert:\n🍽 Pavlova crisp and light meringue dressed with lemon curd and mixed berries.\n.\nStand to win a chance fill a stocking with goodies by just simple spend $120 in single recipe and you got chance to spin the wheel And walk back home with your stuffed stocking or free coffee, tea or sides.\n.\nLocation: @littlehouseofdreams Blk 8, Dempsey Road, #01-14, Singapore 247696\n.\n#littlehouseofdreams #burpplesg #burpple"
        }, {
          "url": "https://www.instagram.com/mightyfoodie/",
          "image": "https://scontent.cdninstagram.com/vp/a45f841c86ff1d51d64191dbfd67721d/5B948189/t51.2885-15/e35/p320x320/24332373_156092631676524_4094029020583165952_n.jpg",
          "caption": "[Christmas @ Little House of Dreams]\n.\nSo pretty 4.5” Baby Reindeer Cakes ($50++)\n.\nLocation: @littlehouseofdreams Blk 8, Dempsey Road, #01-14, Singapore 247696\n.\n#littlehouseofdreams #burpplesg #burpple"
        }, {
          "url": "https://www.instagram.com/mightyfoodie/",
          "image": "https://scontent.cdninstagram.com/vp/d379b8b42e4ee97bf8e8018e082bf13f/5B8608CE/t51.2885-15/s320x320/e35/24845154_169750543625783_6577749599304286208_n.jpg",
          "caption": "[Christmas @ Little House of Dreams]\n.\n3 Course Christmas Menu at $69.90++ per pax which includes a Starter, Main and Dessert is available from 1 to 31 December 2017.\n.\nFeaturing one of the Main course and it’s my fav: Lamb Shank that is braised over 6 hours till tender in red wine sauce.\n.\nLocation: @littlehouseofdreams Blk 8, Dempsey Road, #01-14, Singapore 247696\n.\n#littlehouseofdreams #burpplesg #burpple"
        }, {
          "url": "https://www.instagram.com/mightyfoodie/",
          "image": "https://scontent.cdninstagram.com/vp/2610ba1c4dcb22f3e346cf88c01bce8e/5B88A170/t51.2885-15/e35/p320x320/24838806_1803940649696796_6006566429799743488_n.jpg",
          "caption": "[Christmas @ Little House of Dreams]\n.\nIt’s Friday and this is my feel now! Mr. Snowman lounging in a pool of chocolate at $7.90++\n.\nLocation: @littlehouseofdreams Blk 8, Dempsey Road, #01-14, Singapore 247696\n.\n#littlehouseofdreams #burpplesg #burpple"
        }, {
          "url": "https://www.instagram.com/mightyfoodie/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_40QOsb7gSh_LHs3OslHjtvVSVgcRy-TzJ5sUZ3jUmJo_320x320..jpg",
        }, {
          "url": "https://www.instagram.com/wishuponatart/",
          "image": "https://s3.dualstack.ap-southeast-1.amazonaws.com/mimg-ps/ps_o3qEZHFaYBbOR3CHo0o4ZJbWQ8xY1fssa_d8rKiriZw_320x400..jpg",
        }, {
          "url": "https://www.instagram.com/hungryunicornsg/",
          "image": "https://scontent.cdninstagram.com/vp/4c57b9239d066f2e241d9712a41cc9ed/5B9C3313/t51.2885-15/s320x320/e35/28154089_547030442334446_888579272699019264_n.jpg",
          "caption": "Wishing all 人日快乐！ Soup Restaurant 三盅两件. Located in many locations.. Salmon Yu Sheng (S size - $30.80) Another Yu Sheng which we enjoyed the most during CNY! They used fresh ingredients and Vegs are sliced so thinly! Do share with us which are your fav Yu Sheng too! Let’s all Lo Hei to a better and more HUAT year ahead!! @souprestaurantsg @soup_restaurant_sg #hungryunicornsg #souprestaurant #yusheng #salmonyusheng #yusheng2018"
        }, {
          "url": "https://www.instagram.com/nusfatclub/",
          "image": "https://scontent.cdninstagram.com/vp/589b1778217a75a46715b5eb6e98ba33/5B856929/t51.2885-15/s320x320/e35/28155530_154641685243320_7938036308260159488_n.jpg",
          "caption": "[Serangoon] Their Samsui Ginger Chicken ($22.90) never disappoints— tender, smooth chicken served with that addictively fragrant ginger sauce (which you can buy a bottle of to bring home and snack on). @souprestaurantsg \n#foodporn #vscofood #eeeeeats #vsco #foodie #foodies #vscocam #food #instagood #eat #instadaily #whati8today #foodinsing #sgfoodblogger #foodphotography #eating #lunch #igfood #igfoodie #foodspotting #onthetable #feedfeed #foodstagram #sgfoodie #sgigfoodies #8dayseat #burpple\n\nNo Go\nCan Go👌🏼\nMust Go"
        }, {
          "url": "https://www.instagram.com/nickblitzz/",
          "image": "https://scontent.cdninstagram.com/vp/11c429b384ceac6eb435007aa121cb2b/5B86CE67/t51.2885-15/s320x320/e35/31232788_872422586273204_3997846036897857536_n.jpg",
          "caption": "B-STORY CAFE. Fettuccine Alfredo Truffle Oil Pasta. Alfredo sauce coated the fettuccine well, bacon bits that was crispy and the pasta was drizzled with truffle oil. The rich and earthy taste of the truffle really complimented the pasta dish. I also ordered a blueberry smoothie to pair with the dish. 🍝\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n⠀⠀⠀⠀⠀⠀⠀⠀⠀\n#sgig #sgcafe #sgeats #sgfood #forkyeah #noodle #noodles #sgfoodporn #instafood #food #foodie #foodporn #foodshare #foodstagram #foodspotting #foreverhungry #foodphotography #eeeeeats #foodgasm #burpple #8dayseat #onthetable #buzzfeast #huffposttaste #feedfeed #onthetableproject #devourpower #thefeedfeed #noodleworship #pasta"
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
