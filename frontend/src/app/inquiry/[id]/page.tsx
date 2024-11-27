'use client';

import React, { useState, useEffect } from "react";
import { useRouter } from "next/router";
import InquiryDetail from "@/components/inquiry/InquiryDetail";

type Inquiry = {
  title: string;
  content: string;
  inquiryStatus: "PENDING" | "ANSWERED"; // 상태는 특정 문자열만 사용
};

export default function InquiryDetailPage() {
  const router = useRouter();
  const { id } = router.query;

  // Inquiry 타입 추가
  const [inquiry, setInquiry] = useState<Inquiry | null>(null);

  useEffect(() => {
    if (!id) return;

    const fetchInquiry = async () => {
      const res = await fetch(`/api/inquiries/${id}`);
      const data = await res.json();
      setInquiry(data);
    };

    fetchInquiry();
  }, [id]);

  if (!inquiry) return <p>로딩 중...</p>;

  return (
      <InquiryDetail
          title={inquiry.title}
          content={inquiry.content}
          inquiryStatus={inquiry.inquiryStatus}
          onStatusChange={() => alert("상태 변경 클릭")}
      />
  );
}
