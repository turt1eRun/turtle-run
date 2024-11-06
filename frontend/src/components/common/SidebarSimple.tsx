import {
  ChevronDown,
  Download,
  Heart,
  MessageCircleMore,
  Play,
  TrendingUp,
  Trophy,
  User
} from "lucide-react";

export default function SidebarSimple() {
  return (
      <div className="h-screen pl-2 pr-2 bg-white" style={{width: '72px'}}>
        <div className="border-b pb-4">
          <div className="group flex items-center flex-col py-2 my-1 rounded-md cursor-pointer">
            <Play className="w-6 h-6 mb-1 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-emerald-500 text-xs">최근</span>
          </div>
          <div className="group flex items-center flex-col py-2 my-1 rounded-md cursor-pointer">
            <TrendingUp className="w-6 h-6 mb-1 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-emerald-500 text-xs">인기</span>
          </div>
          <div className="group flex items-center flex-col py-2 my-1 rounded-md cursor-pointer">
            <Heart className="w-6 h-6 mb-1 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-emerald-500 text-xs">관심</span>
          </div>
          <div className="group flex items-center flex-col py-2 my-1 rounded-md cursor-pointer">
            <Download className="w-6 h-6 mb-1 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-emerald-500 text-xs">대시보드</span>
          </div>
        </div>


        <div className="border-b pb-4 pt-4">
          <div className="group flex items-center flex-col py-2 my-1 rounded-md cursor-pointer">
            <User className="w-6 h-6 text-gray-500 group-hover:text-emerald-500"/>
          </div>
          <div className="group flex items-center flex-col py-2 my-1 rounded-md cursor-pointer">
            <User className="w-6 h-6 text-gray-500 group-hover:text-emerald-500"/>
          </div>
          <div className="group flex items-center flex-col py-2 my-1 rounded-md cursor-pointer">
            <User className="w-6 h-6 text-gray-500 group-hover:text-emerald-500"/>
          </div>
          <div className="flex items-center flex-col py-2 my-1 rounded-md cursor-pointer">
            <ChevronDown className="w-6 h-6 text-gray-500"/>
          </div>
        </div>

        <div className="pt-4">
          <div className="group flex items-center flex-col py-2 my-1 rounded-md cursor-pointer">
            <MessageCircleMore className="w-6 h-6 mb-1 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-emerald-500 text-xs">고객센터</span>
          </div>
          <div className="group flex items-center flex-col py-2 my-1 rounded-md cursor-pointer">
            <Trophy className="w-6 h-6 mb-1 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-emerald-500 text-xs">랭킹</span>
          </div>
        </div>

      </div>
  )
}
