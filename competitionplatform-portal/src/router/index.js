import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/pages/Index/index'
import Competitions from '@/pages/Competitions/competitions'
import Competition from '@/pages/Competitions/competition'
import Videos from '@/pages/Videos/videos'
import Video from '@/pages/Videos/video'
import Bbs from '@/pages/Bbs/bbs'
import Notfound from '@/pages/Index/notfound'
import Personal from '@/pages/Index/personal'
import Post from '@/pages/Bbs/post'
import ReleasePost from '@/pages/Bbs/release'
import Register from '@/pages/Index/register'
import Activate from '@/pages/Index/activate'
import AdminLogin from '@/pages/Admin/login'
import Admin from '@/pages/Admin/admin'
import EditCompetition from '@/pages/admin/edit/competition'
import ReleaseCompetition from '@/pages/admin/release/competition'
import EditVideo from '@/pages/admin/edit/video'
import ReleaseVideo from '@/pages/admin/release/video'
import EditPost from '@/pages/admin/edit/post'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: Index
    },
    {
      path: '*',
      name: 'lost',
      component: Notfound
    },
    {
      path: '/competitions',
      name: 'competitions',
      component: Competitions
    },
    {
      path: '/competition/:id',
      name: 'competition',
      component: Competition
    },
    {
      path: '/videos',
      name: 'videos',
      component: Videos
    },
    {
      path: '/video/:id',
      name: 'video',
      component: Video
    },
    {
      path: '/bbs',
      name: 'bbs',
      component: Bbs
    },
    {
      path: '/notfound',
      name: 'notfound',
      component: Notfound
    },
    {
      path: '/personal',
      name: 'personal',
      component: Personal
    },
    {
      path: '/post/:id',
      name: 'post',
      component: Post
    },
    {
      path: '/releasepost',
      name: 'releasepost',
      component: ReleasePost
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path: '/activate',
      name: 'activate',
      component: Activate
    },
    {
      path: '/admin',
      name: 'admin',
      component: Admin
    },
    {
      path: '/admin/login',
      name: 'adminLogin',
      component: AdminLogin
    },
    {
      path: '/admin/edit/competition/:id',
      name: 'editCompetition',
      component: EditCompetition
    },
    {
      path: '/admin/release/competition',
      name: 'releaseCompetition',
      component: ReleaseCompetition
    },
    {
      path: '/admin/edit/video/:id',
      name: 'editVideo',
      component: EditVideo
    },
    {
      path: '/admin/release/video',
      name: 'releaseVideo',
      component: ReleaseVideo
    },
    {
      path: '/admin/edit/post/:id',
      name: 'editPost',
      component: EditPost
    },
  ]
});

export default router;
