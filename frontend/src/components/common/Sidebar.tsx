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

export default function Sidebar() {
  return (
      <div className="w-60 h-screen px-4 bg-white">
        <div className="border-b pb-4">
          <div className="group flex pl-2 py-2 my-1 rounded-md hover:bg-emerald-100 cursor-pointer">
            <Play className="w-6 h-6 mr-4 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-black font-medium">최근</span>
          </div>
          <div className="group flex pl-2 py-2 my-1 rounded-md hover:bg-emerald-100 cursor-pointer">
            <TrendingUp className="w-6 h-6 mr-4 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-black font-medium">인기</span>
          </div>
          <div className="group flex pl-2 py-2 my-1 rounded-md hover:bg-emerald-100 cursor-pointer">
            <Heart className="w-6 h-6 mr-4 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-black font-medium">관심</span>
          </div>
          <div className="group flex pl-2 py-2 my-1 rounded-md hover:bg-emerald-100 cursor-pointer">
            <Download className="w-6 h-6 mr-4 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-black font-medium">대시보드</span>
          </div>
        </div>


        <div className="border-b pb-4 pt-4">
          <span className="pl-2 text-gray-600">구독</span>
          <div className="flex pl-2 py-2 my-1 rounded-md hover:bg-gray-100 cursor-pointer">
            <User className="w-6 h-6 mr-4 text-gray-500"/>
            <span className="text-gray-800 font-medium">강혜주</span>
          </div>
          <div className="flex pl-2 py-2 my-1 rounded-md hover:bg-gray-100 cursor-pointer">
            <User className="w-6 h-6 mr-4 text-gray-500"/>
            <span className="text-gray-800 font-medium">김래현</span>
          </div>
          <div className="flex pl-2 py-2 my-1 rounded-md hover:bg-gray-100 cursor-pointer">
            <User className="w-6 h-6 mr-4 text-gray-500"/>
            <span className="text-gray-800 font-medium">지승우</span>
          </div>
          <div className="flex pl-2 py-2 my-1 cursor-pointer">
            <ChevronDown className="w-6 h-6 mr-4 text-gray-500"/>
            <span className="text-gray-500">더보기</span>
          </div>
        </div>

        <div className="pt-4">
          <div className="group flex pl-2 py-2 my-1 rounded-md hover:bg-emerald-100 cursor-pointer">
            <MessageCircleMore className="w-6 h-6 mr-4 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-black font-medium">고객센터</span>
          </div>
          <div className="group flex pl-2 py-2 my-1 rounded-md hover:bg-emerald-100 cursor-pointer">
            <Trophy className="w-6 h-6 mr-4 text-gray-500 group-hover:text-emerald-500"/>
            <span className="text-gray-600 group-hover:text-black font-medium">랭킹</span>
          </div>
        </div>

      </div>
  )
}
