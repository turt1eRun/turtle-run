'use client';

export default function InquiryDetailLayout({
                                              children,
                                            }: {
  children: React.ReactNode;
}) {
  return (
      <div>
        <header className="mb-6">
          <h2 className="text-xl font-bold">문의 상세 보기</h2>
        </header>
        {children}
      </div>
  );
}
