import React from "react";
import {Menu, Search} from "lucide-react";
import Link from "next/link";
import {useSidebarStore} from "@/store/sidebarStore";
import Image from "next/image";

export default function Header() {
  const { toggleSidebar } = useSidebarStore()
  return (
      <nav className="flex items-center justify-between px-4 py-2 bg-white">
        {/* Left - Logo */}
        <div className="flex items-center space-x-2">
          <button className="p-2 hover:bg-gray-100 rounded-lg" onClick={ toggleSidebar }>
            <Menu className="w-6 h-6 text-gray-500"/>
          </button>
          <Image src="/images/logo.png" alt='로고' width={88} height={64}/>
        </div>

        {/* Middle - Search Bar */}
        <div className="flex-1 max-w-2xl mx-4">
          <div className="relative">
            <input
                type="text"
                className="w-full px-6 py-2 bg-gray-100 rounded-3xl focus:outline-none focus:ring-2 focus:ring-gray-200"
                placeholder="검색어를 입력하세요"
            />
            <div className="absolute right-3 top-1/2 transform -translate-y-1/2">
              <Search className="w-5 h-5 text-gray-500" />
            </div>
          </div>
        </div>

        {/* Right - Login */}
        <Link href="/login">
          <div className="flex items-center">
            <button className="px-4 py-2 text-sm hover:bg-gray-100 rounded-lg">
              로그인
            </button>
          </div>
        </Link>
      </nav>
  )
}
